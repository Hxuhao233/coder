package org.flysky.coder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Recruitment;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.UserCollectRecruitment;
import org.flysky.coder.entity.UserVoteRecruitment;
import org.flysky.coder.entity.wrapper.RecruitmentWrapper;
import org.flysky.coder.mapper.RecruitmentMapper;
import org.flysky.coder.mapper.UserCollectRecruitmentMapper;
import org.flysky.coder.mapper.UserVoteRecruitmentMapper;
import org.flysky.coder.service.IRecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hxuhao233 on 2018/4/26.
 */
@Service
public class RecruitmentService implements IRecruitmentService {

    @Autowired
    private RecruitmentMapper recruitmentMapper;

    @Autowired
    private UserVoteRecruitmentMapper userVoteRecruitmentMapper;

    @Autowired
    private UserCollectRecruitmentMapper userCollectRecruitmentMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public int createRecruitment(Recruitment recruitment) {
        return recruitmentMapper.insertSelective(recruitment);
    }

    @Override
    public Recruitment getRecruitmentById(int id) {
        return recruitmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public RecruitmentWrapper getRecruitmentWrapperById(int id) {
        return recruitmentMapper.getRecruitmentWrapperById(id);
    }

    @Override
    public boolean hasRecruitmentName(String name) {
        return recruitmentMapper.hasRecruitmentName(name);
    }

    @Override
    public int voteRecruitment(Recruitment recruitment, UserVoteRecruitment userVoteRecruitment) {

        // 删除以前的
        UserVoteRecruitment prevVoteRecruitment = userVoteRecruitmentMapper.getUserVoteRecruitmentByUserIdAndRecruitmentId(userVoteRecruitment.getUserId(), recruitment.getId());
        if (prevVoteRecruitment != null) {
            if (prevVoteRecruitment.getType().equals(UserVoteRecruitment.TYPE_UP_VOTE)) {
                recruitment.setUpvote(recruitment.getUpvote() - 1);
            } else if (prevVoteRecruitment.getType().equals(UserVoteRecruitment.TYPE_DOWN_VOTE)) {
                recruitment.setDownvote(recruitment.getDownvote() - 1);
            }
            userVoteRecruitmentMapper.deleteByPrimaryKey(prevVoteRecruitment.getId());
        }

        if (userVoteRecruitment.getType().equals(UserVoteRecruitment.TYPE_UP_VOTE)) {
            recruitment.setUpvote(recruitment.getUpvote() + 1);
        } else if (userVoteRecruitment.getType().equals(UserVoteRecruitment.TYPE_DOWN_VOTE)){
            recruitment.setDownvote(recruitment.getDownvote() + 1);
        }
        recruitmentMapper.updateByPrimaryKeySelective(recruitment);

        return userVoteRecruitmentMapper.insertSelective(userVoteRecruitment);
    }

    @Override
    public int undoVoteRecruitment(Recruitment recruitment, User user) {
        UserVoteRecruitment userVoteRecruitment = userVoteRecruitmentMapper.getUserVoteRecruitmentByUserIdAndRecruitmentId(user.getId(), recruitment.getId());
        if (userVoteRecruitment == null) {
            return 0;
        }

        if (userVoteRecruitment.getType().equals(UserVoteRecruitment.TYPE_UP_VOTE)) {
            recruitment.setUpvote(recruitment.getUpvote() - 1);
        } else if (userVoteRecruitment.getType().equals(UserVoteRecruitment.TYPE_DOWN_VOTE)) {
            recruitment.setDownvote(recruitment.getDownvote() - 1);
        }
        recruitmentMapper.updateByPrimaryKeySelective(recruitment);

        return userVoteRecruitmentMapper.deleteByPrimaryKey(recruitment.getId());
    }

    @Override
    public UserVoteRecruitment getVoteRecruitment(int userId, int recruitmentId){
        return userVoteRecruitmentMapper.getUserVoteRecruitmentByUserIdAndRecruitmentId(userId, recruitmentId);
    }

    @Override
    public int collectRecruitment(Recruitment recruitment, UserCollectRecruitment userCollectRecruitment) {
        recruitment.setCollection(recruitment.getCollection() + 1);
        recruitmentMapper.updateByPrimaryKeySelective(recruitment);

        return userCollectRecruitmentMapper.insertSelective(userCollectRecruitment);
    }

    @Override
    public int undoCollectRecruitment(Recruitment recruitment, User user) {
        UserCollectRecruitment userCollectRecruitment = userCollectRecruitmentMapper.getUserCollectRecruitmentByUserIdAndRecruitmentId(user.getId(), recruitment.getId());
        if (userCollectRecruitment == null){
            return 0;
        }
        recruitment.setCollection(recruitment.getCollection() - 1);
        recruitmentMapper.updateByPrimaryKeySelective(recruitment);

        return userCollectRecruitmentMapper.deleteByPrimaryKey(userCollectRecruitment.getId());
    }

    @Override
    public UserCollectRecruitment getCollectRecruitment(int userId, int recruitmentId){
        return userCollectRecruitmentMapper.getUserCollectRecruitmentByUserIdAndRecruitmentId(userId, recruitmentId);
    }

    @Override
    public int deleteRecruitment(Recruitment recruitment) {
        recruitment.setIsDeleted(true);
        return recruitmentMapper.updateByPrimaryKeySelective(recruitment);
    }


    @Override
    public PageInfo<RecruitmentWrapper> getRecruitmentWrappersByInfo(String info, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<>(recruitmentMapper.getRecruitmentWrappersByInfo(info));
    }


    @Override
    public int putTopRecruitment(int recruitmentId) {
        redisTemplate.opsForSet().add("topRecruitments",String.valueOf(recruitmentId));
        return 0;
    }


    @Override
    public int undoPutTopRecruitment(int recruitmentId) {
        redisTemplate.opsForSet().remove("topRecruitments",String.valueOf(recruitmentId));
        return 0;
    }

    @Override
    public List<RecruitmentWrapper> getTopRecruitments() {
        Set<String> topRecruitmentIds = redisTemplate.opsForSet().members("topRecruitments");
        List<RecruitmentWrapper> topRecruitments = new ArrayList<>();
        for (String topRecruitmentId : topRecruitmentIds) {
            topRecruitments.add(recruitmentMapper.getRecruitmentWrapperById(Integer.valueOf(topRecruitmentId)));
        }
        return topRecruitments;
    }

    @Override
    public int recommendRecruitment(int recruitmentId) {
        redisTemplate.opsForSet().add("commendedRecruitments",String.valueOf(recruitmentId));
        return 0;
    }

    @Override
    public int undoRecommendRecruitment(int recruitmentId) {
        redisTemplate.opsForSet().remove("commendedRecruitments",String.valueOf(recruitmentId));
        return 0;
    }

    @Override
    public List<RecruitmentWrapper> getRecommendedRecruitments() {
        Set<String> topRecruitmentIds = redisTemplate.opsForSet().members("commendedRecruitments");
        List<RecruitmentWrapper> topRecruitments = new ArrayList<>();
        for (String topRecruitmentId : topRecruitmentIds) {
            topRecruitments.add(recruitmentMapper.getRecruitmentWrapperById(Integer.valueOf(topRecruitmentId)));
        }
        return topRecruitments;
    }


}
