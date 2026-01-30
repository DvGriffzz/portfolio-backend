package com.diogo.portfolio_backend.service;

import com.diogo.portfolio_backend.entity.Skill;
import com.diogo.portfolio_backend.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Long id, Skill updatedSkill) {
        return skillRepository.findById(id)
                .map(skill -> {
                    skill.setName(updatedSkill.getName());
                    skill.setLevel(updatedSkill.getLevel());
                    return skillRepository.save(skill);
                })
                .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}

