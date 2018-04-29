package org.flysky.coder.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.Recruitment;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.UserCollectRecruitment;
import org.flysky.coder.entity.UserVoteRecruitment;
import org.flysky.coder.entity.wrapper.RecruitmentWrapper;
import org.flysky.coder.service.IRecruitmentService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.VoteInfo;
import org.flysky.coder.vo.recruitment.RecruitmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/26.
 */
@RestController
public class RecruitmentController {

    @Autowired
    private IRecruitmentService recruitmentService;

    /**
     * 创建招聘
     * @param session
     * @param recruitmentInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment", method = RequestMethod.POST)
    public Result createRecruitment(HttpSession session, @RequestBody RecruitmentInfo recruitmentInfo){
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        ResultWrapper result = new ResultWrapper();

        if (recruitmentService.hasRecruitmentName(recruitmentInfo.getTitle())){
            result.setCode(0);
            result.setInfo("已存在该招聘");
            return result;
        }

        Recruitment recruitment = new Recruitment();
        recruitment.setTitle(recruitmentInfo.getTitle());
        recruitment.setType(recruitmentInfo.getType());
        recruitment.setIsDeleted(false);
        recruitment.setCollection(0);
        recruitment.setCreatedAt(time);
        recruitment.setUpdatedAt(time);
        recruitment.setDownvote(0);
        recruitment.setPhone(recruitmentInfo.getPhone());
        recruitment.setRequirement(recruitmentInfo.getRequirement());
        recruitment.setSalary(recruitment.getSalary());
        recruitment.setUpvote(0);
        recruitment.setUserId(user.getId());
        recruitmentService.createRecruitment(recruitment);

        result.setCode(1);
        result.setPayload(recruitment);
        return result;
    }

    /**
     * 查看招聘
     * @param recruitmentId
     * @return
     */
    @RequestMapping(value = "/recruitment/{recruitmentId}", method = RequestMethod.GET)
    public ResultWrapper getRecruitmentById(@PathVariable(value = "recruitmentId") int recruitmentId) {
        ResultWrapper resultWrapper = new ResultWrapper();
        RecruitmentWrapper recruitmentWrapper = recruitmentService.getRecruitmentWrapperById(recruitmentId);
        if (recruitmentWrapper == null){
            resultWrapper.setCode(2);
            resultWrapper.setInfo("不存在此招聘");
        } else {
            resultWrapper.setCode(1);
            resultWrapper.setPayload(recruitmentWrapper);
        }
        return resultWrapper;
    }

    /**
     * 对招聘点赞/踩
     * @param session
     * @param recruitmentId
     * @param voteInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment/{recruitmentId}/vote", method = RequestMethod.POST)
    public Result voteRecruitment(HttpSession session, @PathVariable(value = "recruitmentId") int recruitmentId, @RequestBody VoteInfo voteInfo) {
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null){
            result.setCode(2);
            result.setInfo("不存在此招聘");
        }

        UserVoteRecruitment userVoteRecruitment = new UserVoteRecruitment();
        userVoteRecruitment.setCreatedAt(time);
        userVoteRecruitment.setRecruitmentId(recruitmentId);
        userVoteRecruitment.setType(voteInfo.getType());
        userVoteRecruitment.setUserId(user.getId());
        recruitmentService.voteRecruitment(recruitment, userVoteRecruitment);

        result.setCode(1);
        return result;

    }

    /**
     * 查看对招聘的点赞/踩状态
     * @param session
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment/{recruitmentId}/vote", method = RequestMethod.GET)
    public ResultWrapper getVoteRecruitment(HttpSession session, @PathVariable(value = "recruitmentId") int recruitmentId) {
        ResultWrapper resultWrapper = new ResultWrapper();
        User user = (User) session.getAttribute("user");

        UserVoteRecruitment userVoteRecruitments = recruitmentService.getVoteRecruitment(user.getId(), recruitmentId);
        resultWrapper.setCode(1);
        resultWrapper.setPayload(userVoteRecruitments);
        return resultWrapper;
    }

    /**
     * 撤销对招聘点赞/踩
     * @param session
     * @param recruitmentId
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment/{recruitmentId}/vote", method = RequestMethod.DELETE)
    public Result undoVoteRecruitment(HttpSession session, @PathVariable(value = "recruitmentId") int recruitmentId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null){
            result.setCode(2);
            result.setInfo("不存在此招聘");
        }

        recruitmentService.undoVoteRecruitment(recruitment, user);

        result.setCode(1);
        return result;
    }

    /**
     * 收藏招聘
     * @param session
     * @param recruitmentId
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment/{recruitmentId}/collect", method = RequestMethod.POST)
    public Result collectRecruitment(HttpSession session, @PathVariable(value = "recruitmentId") int recruitmentId) {
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null){
            result.setCode(2);
            result.setInfo("不存在此招聘");
        }

        UserCollectRecruitment userCollectRecruitment = new UserCollectRecruitment();
        userCollectRecruitment.setCreatedAt(time);
        userCollectRecruitment.setRecruitmentId(recruitmentId);
        userCollectRecruitment.setUserId(user.getId());
        recruitmentService.collectRecruitment(recruitment, userCollectRecruitment);

        result.setCode(1);
        return result;
    }

    /**
     * 查看对招聘 收藏状态
     * @param session
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment/{recruitmentId}/collect", method = RequestMethod.GET)
    public ResultWrapper getCollectRecruitment(HttpSession session, @PathVariable(value = "recruitmentId") int recruitmentId) {
        ResultWrapper resultWrapper = new ResultWrapper();
        User user = (User) session.getAttribute("user");

        UserCollectRecruitment userCollectRecruitment = recruitmentService.getCollectRecruitment(user.getId(), recruitmentId);
        resultWrapper.setPayload(userCollectRecruitment);
        return resultWrapper;
    }



    /**
     * 撤销收藏招聘
     * @param session
     * @param recruitmentId
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/recruitment/{recruitmentId}/collect", method = RequestMethod.DELETE)
    public Result undoCollectRecruitment(HttpSession session, @PathVariable(value = "recruitmentId") int recruitmentId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null){
            result.setCode(2);
            result.setInfo("不存在此招聘");
        }

        recruitmentService.undoCollectRecruitment(recruitment, user);

        result.setCode(1);
        return result;
    }

    /**
     * 搜索招聘
     * @param info
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/recruitment", method = RequestMethod.GET)
    public Result getRecruitmentWrappersByInfo(@RequestParam(value = "info") String info, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultWrapper resultWrapper = new ResultWrapper();
        PageInfo<RecruitmentWrapper> recruitmentWrappers = recruitmentService.getRecruitmentWrappersByInfo(info, pageNum, pageSize);
        if (recruitmentWrappers.getSize() > 0) {
            resultWrapper.setCode(1);
            resultWrapper.setPayload(recruitmentWrappers);
        } else {
            resultWrapper.setCode(2);
        }
        return resultWrapper;
    }

    /**
     * 置顶招聘
     * @param recruitmentId
     * @return
     */
    //@RequiresRoles(value = "manager")
    @RequestMapping(value = "/topRecruitment/{recruitmentId}", method = RequestMethod.POST)
    public Result putTopRecruitment(@PathVariable("recruitmentId") int recruitmentId){
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null) {
            result.setCode(2);
            result.setInfo("不存在此招聘");
            return result;
        }

        recruitmentService.putTopRecruitment(recruitmentId);
        result.setCode(1);
        return result;
    }

    /**
     * 取消置顶招聘
     * @param recruitmentId
     * @return
     */
    //@RequiresRoles(value = "manager")
    @RequestMapping(value = "/topRecruitment/{recruitmentId}", method = RequestMethod.DELETE)
    public Result undoPutTopRecruitment(@PathVariable(value = "recruitmentId") int recruitmentId){
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null) {
            result.setCode(2);
            result.setInfo("不存在此招聘");
            return result;
        }

        recruitmentService.undoPutTopRecruitment(recruitmentId);
        result.setCode(1);
        return result;
    }

    /**
     * 获取置顶招聘
     * @return
     */
    @RequestMapping(value = "/topRecruitment", method = RequestMethod.GET)
    public Result getTopRecruitments() {
        ResultWrapper result = new ResultWrapper();
        result.setCode(1);
        result.setPayload(recruitmentService.getTopRecruitments());
        return result;
    }


    /**
     * 推荐招聘
     * @param recruitmentId
     * @return
     */
    //@RequiresRoles(value = "manager")
    @RequestMapping(value = "/recommendedRecruitment/{recruitmentId}", method = RequestMethod.POST)
    public Result commendRecruitment(@PathVariable("recruitmentId") int recruitmentId){
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null) {
            result.setCode(2);
            result.setInfo("不存在此招聘");
            return result;
        }

        recruitmentService.recommendRecruitment(recruitmentId);
        result.setCode(1);
        return result;
    }

    /**
     * 取消推荐招聘
     * @param recruitmentId
     * @return
     */
    //@RequiresRoles(value = "manager")
    @RequestMapping(value = "/recommendedRecruitment/{recruitmentId}", method = RequestMethod.DELETE)
    public Result undocommendRecruitment(@PathVariable(value = "recruitmentId") int recruitmentId){
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null) {
            result.setCode(2);
            result.setInfo("不存在此招聘");
            return result;
        }

        recruitmentService.undoRecommendRecruitment(recruitmentId);
        result.setCode(1);
        return result;
    }

    /**
     * 获取推荐招聘
     * @return
     */
    @RequestMapping(value = "/recommendedRecruitment", method = RequestMethod.GET)
    public Result getcommendRecruitments() {
        ResultWrapper result = new ResultWrapper();
        result.setCode(1);
        result.setPayload(recruitmentService.getRecommendedRecruitments());
        return result;
    }

    /**
     * 删除招聘
     * @param recruitmentId
     * @return
     */
    //@RequiresRoles(value = "manager")
    @RequestMapping(value = "/recruitment/{recruitmentId}", method = RequestMethod.DELETE)
    public Result deleteRecruitment(@PathVariable(value = "recruitmentId")int recruitmentId) {
        Result result = new Result();
        Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
        if (recruitment == null) {
            result.setCode(2);
            result.setInfo("不存在此招聘");
            return result;
        }

        recruitmentService.deleteRecruitment(recruitment);
        result.setCode(1);
        return result;

    }
}
