package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Recruitment;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.UserCollectRecruitment;
import org.flysky.coder.entity.UserVoteRecruitment;
import org.flysky.coder.entity.wrapper.RecruitmentWrapper;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/26.
 */
public interface IRecruitmentService {
    int createRecruitment(Recruitment recruitment);

    Recruitment getRecruitmentById(int id);

    RecruitmentWrapper getRecruitmentWrapperById(int id);

    boolean hasRecruitmentName(String name);

    int voteRecruitment(Recruitment recruitment, UserVoteRecruitment userVoteRecruitment);

    int undoVoteRecruitment(Recruitment recruitment, User user);

    UserVoteRecruitment getVoteRecruitment(int userId, int recruitmentId);

    int collectRecruitment(Recruitment recruitment, UserCollectRecruitment userCollectRecruitment);

    int undoCollectRecruitment(Recruitment recruitment, User user);

    UserCollectRecruitment getCollectRecruitment(int userId, int recruitmentId);

    int deleteRecruitment(Recruitment recruitment);

    PageInfo<RecruitmentWrapper> getRecruitmentWrappersByInfo(String info, int pageNum, int pageSize);

    int putTopRecruitment(int recruitmentId);

    int undoPutTopRecruitment(int recruitmentId);

    List<RecruitmentWrapper> getTopRecruitments();

    int recommendRecruitment(int recruitmentId);

    int undoRecommendRecruitment(int recruitmentId);

    List<RecruitmentWrapper> getRecommendedRecruitments();
}
