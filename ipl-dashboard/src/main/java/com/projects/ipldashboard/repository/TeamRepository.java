package com.projects.ipldashboard.repository;

import com.projects.ipldashboard.Model.Team;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
    
    /**
     * This method will fetch an instance from Team against the specified teamName param
     */
    public Team getByTeamName(String teamName);

}
