package online.referity.service;

import online.referity.dto.request.SkillListRequest;
import online.referity.entity.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public interface SkillService {


    public Skill createSkill(Skill skillName, UUID groupId);

    public SkillGroup createGroup(SkillGroup GroupName);

    public List<Skill> createSkill(HashMap<String, UUID> skillName);

    public SkillLevel addSkillCandidate(Skill skill, CV cv, int point);
    public SkillLevel addSkillJob(Skill skill, Job job , int point );

    public  SkillLevel updateSkillLevel(UUID skillLevelId , SkillListRequest skillListRequest);

    public Skill update(Skill data, UUID skillId);

    public SkillGroup updateGroup(SkillGroup data, UUID skillGroupId);

    public Skill delete(UUID skillId);

    public SkillGroup deleteGroup(UUID skillGroupId);

    public List<SkillLevel> getSkillbyId(UUID id, String type);

    public List<Skill> seachSkill(String key);

    public List<SkillGroup> seachGroup();

    public List<SkillGroup> createGroups(List<String> groups);

    public List<Skill> getSkill();



}
