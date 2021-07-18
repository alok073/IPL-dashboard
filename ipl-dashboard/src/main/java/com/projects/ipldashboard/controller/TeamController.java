package com.projects.ipldashboard.controller;

import java.time.LocalDate;
import java.util.List;

import com.projects.ipldashboard.Model.Match;
import com.projects.ipldashboard.Model.Team;
import com.projects.ipldashboard.repository.MatchRepository;
import com.projects.ipldashboard.repository.TeamRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team")
    public Iterable<Team> getAllTeams() {
        return this.teamRepository.findAll();
        
    }
    
    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {

        Team team = this.teamRepository.getByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatches(teamName));

        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {

        LocalDate startDate = LocalDate.of(year, 1, 1); // startDate = 01/01/year
        LocalDate endDate = LocalDate.of(year+1, 1, 1); // startDate = 01/01/year+1

        return this.matchRepository.getMatchesByTeamBetweenDates(
            teamName, 
            startDate, 
            endDate);
    }

}
