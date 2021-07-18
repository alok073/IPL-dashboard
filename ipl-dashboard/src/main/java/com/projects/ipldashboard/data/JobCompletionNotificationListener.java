package com.projects.ipldashboard.data;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.projects.ipldashboard.Model.Team;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Transactional
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("!!! JOB FINISHED! Time to verify the results");

            /**
             * HashMap -> key=teamName & value=Team instance
             * After writing the queries....no. of keys in hashMap=no. of teams that have played IPL and against each team the Team instance
             * will store the total matches they have played and total matches they have won
             */

            Map<String, Team> teamData = new HashMap<>();

            em.createQuery("select distinct m.team1, count(*) from Match m group by m.team1", Object[].class)
              .getResultList()
              .stream()
              .map(e -> new Team((String) e[0], (long) e[1]))
              .forEach(team -> teamData.put(team.getTeamName(), team));

              em.createQuery("select distinct m.team2, count(*) from Match m group by m.team2", Object[].class)
              .getResultList()
              .stream()
              .forEach(e -> {
                Team team = teamData.get((String) e[0]);
                team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
              });

              em.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
              .getResultList()
              .stream()
              .forEach(e -> {
                Team team = teamData.get((String) e[0]);
                if(team != null) team.setTotalWins((long) e[1]);
              });

              teamData.values().forEach(team -> em.persist(team));
              teamData.values().forEach(team -> System.out.println(team));
              
        }
    }
}
