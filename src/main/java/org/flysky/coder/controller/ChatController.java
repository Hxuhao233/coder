package org.flysky.coder.controller;

import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.flysky.coder.service.IChatService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.chat.ChatMessage;
import org.flysky.coder.vo.chat.HomeInfo;
import org.flysky.coder.vo.chat.RecordPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @RequestMapping(value = "/home",method = RequestMethod.POST)
    public Result createHome(@RequestBody HomeInfo homeInfo){
        LocalDateTime time = LocalDateTime.now();

        Home home = new Home();
        home.setName(homeInfo.getName());
        home.setDescription(homeInfo.getDescription());
        home.setCreatedAt(time);
        home.setUpdatedAt(time);
        home.setIsDeleted(false);
        int code = chatService.createHome(home);

        Result result = new Result(code);
        if (code == 1) {
            result.setInfo("创建成功");
        } else {
            result.setInfo("创建失败，名字重复");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.PUT)
    public Result modifyHome(@PathVariable(value = "homeId")int homeId, @RequestBody HomeInfo homeInfo) {
        LocalDateTime time = LocalDateTime.now();

        Home home = new Home();
        home.setId(homeId);
        home.setName(homeInfo.getName());
        home.setDescription(homeInfo.getDescription());
        home.setUpdatedAt(time);
        int code = chatService.modifyHome(home);

        Result result = new Result(code);
        if (code == 1) {
            result.setInfo("修改成功");
        } else {
            result.setInfo("修改失败，名字重复");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/home/{homeId}",method = RequestMethod.GET)
    public Result getHome(@PathVariable(value = "homeId")int homeId) {
        Home home = chatService.getHomeById(homeId);
        ResultWrapper result = new ResultWrapper();
        if (home != null) {
            result.setCode(1);
            result.setPayload(home);
        } else {
            result.setCode(404);
            result.setInfo("不存在此房间");
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
        List<RecordWrapper> recordLists = chatService.getRecord(enterRoomMessage.getRoomId(),1,10);
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
        List<RecordWrapper> recordLists = chatService.getRecord(recordPage.getRoomId(), recordPage.getPageNum(), recordPage.getPageSize());
        template.convertAndSendToUser(headerAccessor.getSessionId(),"/self",recordLists);
    }


}
