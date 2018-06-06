package org.flysky.coder.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.constant.ResponseCode;
import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.entity.wrapper.RoomWrapper;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.chat.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线交流功能控制器
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private IChatService chatService;

    //private ConcurrentHashMap<Integer,String> wsSessionIdMap = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 创建社区
     * @param session
     * @param homeInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/home",method = RequestMethod.POST)
    public Result createHome(HttpSession session, @RequestBody HomeInfo homeInfo){
        LocalDateTime time = LocalDateTime.now();
        User user = (User) session.getAttribute("user");
        ResultWrapper result = new ResultWrapper();

        Home home = new Home();
        home.setName(homeInfo.getName());
        home.setDescription(homeInfo.getDescription());
        home.setCreatedAt(time);
        home.setUpdatedAt(time);
        home.setIsDeleted(false);
        home.setUserId(user.getId());
        int code = chatService.createHome(home);

        if (code == 1) {
            result.setCode(ResponseCode.SUCCEED);
            result.setInfo("创建成功");
            result.setPayload(home);
        } else {
            result.setCode(ResponseCode.DUPLICATE_NAME);
            result.setInfo("创建失败，该社区已存在");
        }
        return result;
    }

    /**
     * 修改社区
     * @param homeId
     * @param homeInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.PUT)
    public Result modifyHome(HttpSession session, @PathVariable(value = "homeId")int homeId, @RequestBody HomeInfo homeInfo) {
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        ResultWrapper result = new ResultWrapper();

        Home home = chatService.getHomeWrapperById(homeId);
        if (home == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该社区不存在");
        } else if (!home.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            boolean needCheckName;
            if (homeInfo.getName().equals(home.getName())) {
                needCheckName = false;
            }else {
                needCheckName = true;
                home.setName(homeInfo.getName());
            }
            home.setDescription(homeInfo.getDescription());
            home.setUpdatedAt(time);

            int code = chatService.modifyHome(home, needCheckName);

            if (code == 1) {
                result.setCode(ResponseCode.SUCCEED);
                result.setInfo("修改成功");
            } else {
                result.setCode(ResponseCode.DUPLICATE_NAME);
                result.setInfo("修改失败，该社区已存在");
            }
        }
        return result;
    }

    /**
     * 查看社区
     * @param homeId
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.GET)
    public Result getHome(@PathVariable(value = "homeId")int homeId) {
        Home home = chatService.getHomeWrapperById(homeId);
        ResultWrapper result = new ResultWrapper();
        if (home != null) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(home);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该社区不存在");
        }

        return result;
    }

    /**
     * 删除社区
     * @param session
     * @param homeId
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.DELETE)
    public Result deleteHome(HttpSession session, @PathVariable(value = "homeId")int homeId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Home home = chatService.getHomeWrapperById(homeId);
        if (home == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该社区不存在");
        } else if (!home.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            chatService.deleteHome(homeId);
            result.setCode(1);
        }

        return result;
    }

    /**
     * 查看社区列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public Result getHomes(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultWrapper result = new ResultWrapper();
        User user = (User) session.getAttribute("user");
        PageInfo<Home> homes = chatService.getHomeByUserId(user.getId(), pageNum, pageSize);

        if (homes.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(homes);
        } else {
            result.setInfo("你还没有社区");
            result.setCode(ResponseCode.NOT_FOUND);
        }
        return result;
    }

    /**
     * 查看房间列表（某社区下）
     * @param homeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}/rooms", method = RequestMethod.GET)
    public Result getRoomsByHomeId(@PathVariable(value = "homeId") int homeId, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        ResultWrapper result = new ResultWrapper();

        Home home = chatService.getHomeById(homeId);
        if (home == null) {
            result.setCode(ResponseCode.PREV_OBJECT_NOT_FOUND);
            result.setInfo("该社区不存在");
        }

        PageInfo<RoomWrapper> rooms = chatService.getRoomByHomeId(homeId, pageNum, pageSize);
        if (rooms.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(rooms);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该社区下没有房间");
        }

        return result;
    }


    /**
     * 查看个人房间列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/myRooms", method = RequestMethod.GET)
    public Result getRoomsWrapperByUserId(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        ResultWrapper result = new ResultWrapper();
        User user = (User) session.getAttribute("user");


        PageInfo<RoomWrapper> rooms = chatService.getRoomWrappersByUserId(user.getId(), pageNum, pageSize);
        if (rooms.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(rooms);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("你还没有创建房间");
        }

        return result;
    }


    /**
     * 创建房间
     * @param session
     * @param roomInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/room",method = RequestMethod.POST)
    public Result createRoom(HttpSession session, @RequestBody RoomInfo roomInfo) {
        LocalDateTime time = LocalDateTime.now();
        User user = (User) session.getAttribute("user");
        ResultWrapper result = new ResultWrapper();
        /*
        if (chatService.getHomeById(roomInfo.getHomeId()) == null){
            result.setCode(4);
            result.setInfo("创建房间失败，该交流室不存在");
            return result;
        }
        */
        Room room = new Room();
        room.setName(roomInfo.getName());
        room.setDescription(roomInfo.getDescription());
        room.setCreatedAt(time);
        room.setUpdatedAt(time);
        room.setIsDeleted(false);
        //room.setHomeId(roomInfo.getHomeId());
        room.setUserId(user.getId());
        int code = chatService.createRoom(room, roomInfo.getTags());

        if (code == 1) {
            result.setCode(ResponseCode.SUCCEED);
            result.setInfo("创建成功");
            result.setPayload(room);
        } else {
            result.setCode(ResponseCode.DUPLICATE_NAME);
            result.setInfo("创建失败，该房间已存在");
        }
        return result;
    }


    /**
     * 修改房间
     * @param session
     * @param roomInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/room/{roomId}",method = RequestMethod.PUT)
    public Result modifyRoom(HttpSession session, @PathVariable(value = "roomId") int roomId, @RequestBody RoomInfo roomInfo) {
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();
        User user = (User) session.getAttribute("user");

        Room room = chatService.getRoomById(roomId);
        if (room == null) {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该房间不存在");
        } else if (!room.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            boolean needCheckName;
            if (roomInfo.getName().equals(room.getName())) {
                needCheckName = false;
            }else {
                needCheckName = true;
                room.setName(roomInfo.getName());
            }

            room.setDescription(roomInfo.getDescription());
            room.setUpdatedAt(time);

            int code = chatService.modifyRoom(room, needCheckName, roomInfo.getTags());
            if (code == 1) {
                result.setCode(ResponseCode.SUCCEED);
                result.setInfo("修改成功");
            } else {
                result.setCode(ResponseCode.DUPLICATE_NAME);
                result.setInfo("修改失败，该房间已存在");
            }
        }
        return result;
    }



    /**
     * 查看房间
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.GET)
    public Result getRoom(@PathVariable(value = "roomId")int roomId) {
        Room room = chatService.getRoomWrapperById(roomId);
        ResultWrapper result = new ResultWrapper();
        if (room != null) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(room);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该房间不存在");
        }
        return result;
    }


    /** 删除房间
     * @param session
     * @param roomId
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/room/{roomId}",method = RequestMethod.DELETE)
    public Result deleteRoom(HttpSession session, @PathVariable(value = "roomId")int roomId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Room room = chatService.getRoomById(roomId);
        if (room == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该房间不存在");
        } else if (!room.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            chatService.deleteRoom(roomId);
            result.setCode(ResponseCode.SUCCEED);
        }

        return result;
    }


    /**
     * 搜索房间
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/room", method = RequestMethod.GET)
    public Result searchRoom(@RequestParam(value = "info")String info, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        PageInfo<RoomWrapper> rooms = chatService.getRoomByInfo(info, pageNum, pageSize);
        ResultWrapper result = new ResultWrapper();
        if (rooms.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(rooms);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("搜索结果为空");
        }
        return result;
    }

    /**
     * 获取历史房间
     * @param session
     * @return
     */
    @ResponseBody
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/historyRooms", method = RequestMethod.GET)
    public Result getHistoryRooms(HttpSession session){
        User user = (User) session.getAttribute("user");
        ResultWrapper result = new ResultWrapper();

        List<Room> rooms = chatService.getHistoryRoom(user.getId());
        if (rooms.size() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(rooms);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
        }
        return result;
    }

    /**
     * 删除历史房间
     * @param session
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/historyRoom/{roomId}", method = RequestMethod.DELETE)
    public Result deleteHistoryRoom(HttpSession session, @PathVariable(value = "roomId") int roomId){
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        chatService.deleteHistoryRoom(user.getId(), roomId);
        result.setCode(ResponseCode.SUCCEED);
        return result;
    }

    /**
     * 获取房间在线用户
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/room/{roomId}/onlineUsers", method = RequestMethod.GET)
    public Result getOnlineUsers(@PathVariable(value = "roomId") int roomId){
        ResultWrapper result = new ResultWrapper();

        result.setPayload(chatService.getOnlineUsersByRoomId(roomId));
        result.setCode(ResponseCode.SUCCEED);
        return result;
    }

    /**
     * 初始化，保存sessionId
     * @param headerAccessor
     * @throws Exception
     */
    //@MessageMapping("/init")
    public void init(SimpMessageHeaderAccessor headerAccessor) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");
        //wsSessionIdMap.put(user.getId(), String.valueOf(user.getId()));
    }

    /**
     * 加入聊天
     * @param headerAccessor
     * @param enterRoomMessage
     * @throws Exception
     */
    @MessageMapping("/enter")
    public void enter(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage enterRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.enterRoom(user,enterRoomMessage);
        template.convertAndSend("/chat/" + response.getToId(),response);

        // 获取最近10条聊天记录
        //PageInfo<RecordWrapper> recordLists = chatService.getRecord(enterRoomMessage.getToId(), enterRoomMessage.getToId(),1,10);
        //template.convertAndSendToUser(user.getId(),"/self",recordLists);
    }


    /**
     * 退出聊天
     * @param headerAccessor
     * @param exitRoomMessage
     * @throws Exception
     */
    @MessageMapping("/exit")
    public void exit(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage exitRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.exitRoom(user,exitRoomMessage);
        template.convertAndSend("/chat/" + response.getToId(),response);
    }

    /**
     * 交流
     * @param chatMessage
     */
    @MessageMapping("/chat")
    public void chat(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage chatMessage) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.chat(user,chatMessage);
        if (chatMessage.getType()==ChatMessage.TYPE_ROOM_CHAT)
            template.convertAndSend("/chat/" + response.getToId(), response);
        //else if (wsSessionIdMap.get(chatMessage.getToId())!=null)
        //    template.convertAndSendToUser(String.valueOf(user.getId()),"/self",response);
    }

    @MessageMapping("/getRecordByToIdAndPage")
    public void getMessageRecordByPageNum(SimpMessageHeaderAccessor headerAccessor, @RequestBody RecordPage recordPage) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        PageInfo<RecordWrapper> recordLists = chatService.getRecord(recordPage.getToId(), recordPage.getType(), recordPage.getPageNum(), recordPage.getPageSize());
        template.convertAndSendToUser(String.valueOf(user.getId()),"/self",recordLists);
    }

    @MessageMapping("/getRecordByToIdAndLastTime")
    public void getMessageRecordByLastTime(SimpMessageHeaderAccessor headerAccessor, @RequestBody RecordDate recordDate) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");
        List<RecordWrapper> recordLists = chatService.getRecord(recordDate.getToId(), recordDate.getType(), recordDate.getTime());
        HashMap<String,Object> recordListsMap = new HashMap<>();
        recordListsMap.put("list",recordLists);
        template.convertAndSendToUser(String.valueOf(user.getId()),"/self",recordListsMap);
    }


}
