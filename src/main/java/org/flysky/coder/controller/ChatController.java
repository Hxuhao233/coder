package org.flysky.coder.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.Room;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.entity.wrapper.RoomWrapper;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.chat.ChatMessage;
import org.flysky.coder.vo.chat.HomeInfo;
import org.flysky.coder.vo.chat.RecordPage;
import org.flysky.coder.vo.chat.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线交流功能控制器
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private IChatService chatService;

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

        Home home = new Home();
        home.setName(homeInfo.getName());
        home.setDescription(homeInfo.getDescription());
        home.setCreatedAt(time);
        home.setUpdatedAt(time);
        home.setIsDeleted(false);
        home.setUserId(user.getId());
        int code = chatService.createHome(home);

        Result result = new Result(code);
        if (code == 1) {
            result.setInfo("创建成功");
        } else {
            result.setInfo("创建失败，名字重复");
        }
        return result;
    }

    /**
     * 修改社区
     * @param homeId
     * @param homeInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.PUT)
    public Result modifyHome(@PathVariable(value = "homeId")int homeId, @RequestBody HomeInfo homeInfo) {
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();

        Home home = chatService.getHomeById(homeId);
        if (home == null){
            result.setCode(2);
            result.setInfo("不存在社区");
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
            result.setCode(code);
            if (code == 1) {
                result.setInfo("修改成功");
            } else {
                result.setInfo("修改失败，名字重复");
            }
        }
        return result;
    }

    /**
     * 查看社区
     * @param homeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.GET)
    public Result getHome(@PathVariable(value = "homeId")int homeId) {
        Home home = chatService.getHomeById(homeId);
        ResultWrapper result = new ResultWrapper();
        if (home != null) {
            result.setCode(1);
            result.setPayload(home);
        } else {
            result.setCode(2);
            result.setInfo("不存在此社区");
        }

        return result;
    }

    /**
     * 删除社区
     * @param session
     * @param homeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.DELETE)
    public Result deleteHome(HttpSession session, @PathVariable(value = "homeId")int homeId) {
        User user = (User) session.getAttribute("user");
        int code = chatService.deleteHome(homeId);
        Result result = new Result(code);
        if (code != 0) {
            result.setCode(1);
        } else {
            result.setCode(2);
            result.setInfo("不存在此社区");
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
    @ResponseBody
    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public Result getHomes(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultWrapper result = new ResultWrapper();
        User user = (User) session.getAttribute("user");
        PageInfo<Home> homes = chatService.getHomeByUserId(user.getId(), pageNum, pageSize);

        if (homes.getSize() > 0) {
            result.setCode(1);
            result.setPayload(homes);
        } else {
            result.setInfo("不存在此社区");
            result.setCode(2);
        }

        return result;
    }

    /**
     * 查看房间列表
     * @param homeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/home/{homeId}/rooms", method = RequestMethod.GET)
    public Result getRoomsByHomeId(@PathVariable(value = "homeId") int homeId, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        ResultWrapper result = new ResultWrapper();

        PageInfo<RoomWrapper> rooms = chatService.getRoomByHomeId(homeId, pageNum, pageSize);
        if (rooms.getSize() > 0) {
            result.setCode(1);
            result.setPayload(rooms);
        } else {
            result.setInfo("该社区下没有房间");
            result.setCode(2);
        }

        return result;
    }



    /**
     * 创建房间
     * @param session
     * @param roomInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/room",method = RequestMethod.POST)
    public Result createRoom(HttpSession session, @RequestBody RoomInfo roomInfo) {
        LocalDateTime time = LocalDateTime.now();
        User user = (User) session.getAttribute("user");

        Room room = new Room();
        room.setName(roomInfo.getName());
        room.setDescription(roomInfo.getDescription());
        room.setCreatedAt(time);
        room.setUpdatedAt(time);
        room.setIsDeleted(false);
        room.setHomeId(roomInfo.getHomeId());
        room.setUserId(user.getId());
        int code = chatService.createRoom(room, roomInfo.getTags());

        Result result = new Result(code);
        if (code == 1) {
            result.setInfo("创建成功");
        } else {
            result.setInfo("创建失败，房间名字重复");
        }
        return result;
    }


    /**
     * 修改房间
     * @param session
     * @param roomInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/room/{roomId}",method = RequestMethod.PUT)
    public Result modifyRoom(HttpSession session, @PathVariable(value = "roomId") int roomId, @RequestBody RoomInfo roomInfo) {
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();
        User user = (User) session.getAttribute("user");

        Room room = chatService.getRoomById(roomId);
        if (room == null) {
            result.setCode(2);
            result.setInfo("不存在此房间");
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
            result.setCode(code);
            if (code == 1) {
                result.setInfo("修改成功");
            } else {
                result.setInfo("修改失败，房间名字重复");
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
        Room room = chatService.getRoomById(roomId);
        ResultWrapper result = new ResultWrapper();
        if (room != null) {
            result.setCode(1);
            result.setPayload(room);
        } else {
            result.setCode(2);
            result.setInfo("不存在此房间");
        }
        return result;
    }


    /** 删除房间
     * @param session
     * @param roomId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/room/{roomId}",method = RequestMethod.DELETE)
    public Result deleteRoom(HttpSession session, @PathVariable(value = "roomId")int roomId) {
        User user = (User) session.getAttribute("user");
        int code = chatService.deleteRoom(roomId);
        Result result = new Result(code);
        if (code != 0) {
            result.setCode(1);
        } else {
            result.setCode(2);
            result.setInfo("不存在此房间");
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
            result.setCode(1);
            result.setPayload(rooms);
        } else {
            result.setCode(2);
            result.setInfo("搜索结果为空");
        }
        return result;
    }


    /**
     * 加入聊天室
     * @param headerAccessor
     * @param enterRoomMessage
     * @throws Exception
     */
    @MessageMapping("/enter")
    public void enter(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage enterRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.enterRoom(user,enterRoomMessage);
        template.convertAndSend("/chat/" + response.getRoomId(),response);

        // 获取最近10条聊天记录
        PageInfo<RecordWrapper> recordLists = chatService.getRecord(enterRoomMessage.getRoomId(),1,10);
        template.convertAndSendToUser(headerAccessor.getSessionId(),"/self",recordLists);
    }


    /**
     * 退出聊天室
     * @param headerAccessor
     * @param exitRoomMessage
     * @throws Exception
     */
    @MessageMapping("/exit")
    public void exit(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage exitRoomMessage) throws Exception {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.exitRoom(user,exitRoomMessage);
        template.convertAndSend("/chat/" + response.getRoomId(),response);
    }

    /**
     * 交流
     * @param chatMessage
     */
    @MessageMapping("/chat")
    public void chat(SimpMessageHeaderAccessor headerAccessor, @RequestBody ChatMessage chatMessage) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");

        ChatMessage response = chatService.chat(user,chatMessage);
        template.convertAndSend("/chat/" + response.getRoomId(), response);
    }

    @MessageMapping("/getRecord")
    public void getMessageRecord(SimpMessageHeaderAccessor headerAccessor, @RequestBody RecordPage recordPage) {
        PageInfo<RecordWrapper> recordLists = chatService.getRecord(recordPage.getRoomId(), recordPage.getPageNum(), recordPage.getPageSize());
        template.convertAndSendToUser(headerAccessor.getSessionId(),"/self",recordLists);
    }


}
