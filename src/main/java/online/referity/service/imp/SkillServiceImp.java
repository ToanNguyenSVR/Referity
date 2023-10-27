package online.referity.service.imp;

import online.referity.dto.request.SkillListRequest;
import online.referity.entity.*;
import online.referity.enums.ValidateStatus;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.SkillLevelRepository;
import online.referity.repository.SkillGroupRepository;
import online.referity.repository.SkillRepository;
import online.referity.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillServiceImp implements SkillService {

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    SkillGroupRepository skillGroupRepository;
    @Autowired
    SkillLevelRepository skillLevelRepository;

//    @Autowired
//    SkillJobRepository skillJobRepository;


    @Override
    public Skill createSkill(Skill data, UUID groupId) {

        Optional<SkillGroup> group = skillGroupRepository.findById(groupId);
        if (!group.isPresent()) throw new EntityNotFound("SKill Group Not found ");
        Skill skill = new Skill();
        skill.setSkillName(data.getSkillName());
        skill.setSkillGroup(group.get());
        skill.setValidateStatus(ValidateStatus.ACTIVE);
        return skillRepository.save(skill);


    }

    @Override
    public SkillGroup deleteGroup(UUID skillGroupId) {
        Optional<SkillGroup> skillOptional = skillGroupRepository.findById(skillGroupId);
        if (!skillOptional.isPresent()) throw new EntityNotFound("SKill Group Not found ");
        skillOptional.get().setDelete(true);
        return skillGroupRepository.save(skillOptional.get());
    }

    @Override
    public Skill delete(UUID skillId) {
        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if (!skillOptional.isPresent()) throw new EntityNotFound("SKill  Not found ");
        skillOptional.get().setDelete(true);
        return skillRepository.save(skillOptional.get());
    }

    @Override
    public Skill update(Skill data, UUID skillId) {

        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if (!skillOptional.isPresent()) throw new EntityNotFound("SKill  Not found ");
        Optional<SkillGroup> skillGroupOptional = skillGroupRepository.findById(data.getSkillGroup().getId());
        if (!skillGroupOptional.isPresent()) throw new EntityNotFound("SKill Group Not found ");
        Skill skill = skillOptional.get();
        skill.setSkillName(data.getSkillName());
        skill.setSkillGroup(skillGroupOptional.get());
//        skill.setValidateStatus(data);
        return skillRepository.save(skill);
    }

    @Override
    public SkillGroup updateGroup(SkillGroup data, UUID skillGroupId) {

        Optional<SkillGroup> skillOptional = skillGroupRepository.findById(skillGroupId);
        if (!skillOptional.isPresent()) throw new EntityNotFound("SKill Group Not found ");
        SkillGroup skill = skillOptional.get();
        skill.setGroupName(data.getGroupName());

        return skillGroupRepository.save(skill);
    }


    @Override
    public SkillGroup createGroup(SkillGroup groupName) {

        return skillGroupRepository.save(groupName);
    }

    @Override
    public List<SkillGroup> createGroups(List<String> groups) {
        List<SkillGroup> list = new ArrayList<>();
        for (String g : groups) {
            list.add(new SkillGroup(null, g, false, null));
        }
        return skillGroupRepository.saveAll(list);
    }

    @Override
    public List<Skill> getSkill() {
        return skillRepository.findAll().stream().filter(skillGroup -> !skillGroup.isDelete()).collect(Collectors.toList());
    }




    @Override
    public List<Skill> createSkill(HashMap<String, UUID> skillName) {
        List<Skill> list = new ArrayList<>();
        for (String name : skillName.keySet()) {
            SkillGroup group = skillGroupRepository.getById(skillName.get(name));
            if (group == null) throw new EntityNotFound("SKill Group Not found ");
            list.add(new Skill(null, name, false, null, ValidateStatus.ACTIVE, group));
        }
        return skillRepository.saveAll(list);
    }

    @Override
    public SkillLevel addSkillCandidate(Skill skill, CV cv, int point) {

        SkillLevel skillLevel = skillLevelRepository.checkDuplicate(skill.getId(), cv.getId(), null);
        if (skillLevel != null && skillLevel.getPoint() == point) return skillLevel;
        SkillLevel newSkill = new SkillLevel();
        newSkill.setSkill(skill);
        newSkill.setCv(cv);
        newSkill.setPoint(point);
        return newSkill;
    }

    @Override
    public SkillLevel addSkillJob(Skill skill, Job job, int point) {

        SkillLevel skillLevel = skillLevelRepository.checkDuplicate(null, skill.getId(), job.getId());
        if (skillLevel != null && skillLevel.getPoint() == point) return skillLevel;
        SkillLevel newSkill = new SkillLevel();
        newSkill.setSkill(skill);
        newSkill.setJob(job);
        newSkill.setPoint(point);
        return skillLevelRepository.save(newSkill);
    }

    @Override
    public SkillLevel updateSkillLevel(UUID skillLevelId, SkillListRequest skillListRequest) {
        SkillLevel skillLevel = skillLevelRepository.findSkillLevelById(skillLevelId);
        if(skillLevel == null  ) throw  new BadRequest("Skill level not found");
        Optional<Skill> skill = skillRepository.findById(skillListRequest.getId());
        if(!skill.isPresent()) throw new BadRequest("Skill not found");
        if(skillLevel.getSkill().equals(skill.get())) return skillLevel;
        skillLevel.setSkill(skill.get());
        skillLevel.setPoint(skillListRequest.getPoint());
        return skillLevelRepository.save(skillLevel);
    }

    @Override
    public List<SkillLevel> getSkillbyId(UUID id, String type) {
        List<SkillLevel> list = null;
        switch (type) {
            case "CV":
                list = skillLevelRepository.findByCvId(id);
                break;
            case "JOB":
//               list =  skillLevelRepository.findByJobId(id) ;
                break;
        }
        return list;
    }

    @Override
    public List<Skill> seachSkill(String key) {
        return skillRepository.search(key).stream().filter(skillGroup -> !skillGroup.isDelete()).collect(Collectors.toList());
    }

    @Override
    public List<SkillGroup> seachGroup() {
        return skillGroupRepository.findAll().stream().filter(skillGroup -> !skillGroup.isDelete()).collect(Collectors.toList());
    }


}
