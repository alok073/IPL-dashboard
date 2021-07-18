import { React, useEffect, useState } from 'react';
import { useParams,Link } from 'react-router-dom';
import { MatchDetailCard } from '../components/MatchDetailCard';
import { MatchSmallCard } from '../components/MatchSmallCard';
import { PieChart } from 'react-minimal-pie-chart';

import './TeamPage.scss';

export const TeamPage = () => {

  const [team, setTeam] = useState({ matches: [] });
  const { teamName } = useParams();

  const moreLinkRoute = `/teams/${teamName}/matches/${process.env.REACT_APP_DATA_END_YEAR}`;

  /**
   * Making a REST API call
   */
  useEffect(
    () => {
      const fetchTeam = async () => {
        const response = await fetch(`${process.env.REACT_APP_API_ROOT_URL}/team/${teamName}`);
        const data = await response.json();
        //console.log(data);
        //setting the data field in to the state of the component
        setTeam(data);
      };

      fetchTeam();
    }, [teamName]
  );

  /**
   * Condition to check if user enters wrong teamName or doesn't enter anything
   */
  if (!team || !team.teamName) {
    return <h1>Team not found</h1>
  }

  return (
    <div className="TeamPage">
      <div className="team-name-section">
        <h1 className="team-name">{team.teamName}</h1>
      </div>

      <div className="win-loss-section">
        Wins / Losses
        <PieChart
            data={[
                { 
                  title: 'Losses', 
                  value: team.totalMatches - team.totalWins, 
                  color: '#a34d5d' 
                },
                { 
                  title: 'Wins', 
                  value: team.totalWins, 
                  color: '#4da375' 
                },
            ]}
            />
      </div>

      <div className="match-detail-section">
        <h3>Latest Matches</h3>
        <MatchDetailCard teamName={team.teamName} match={team.matches[0]}/>
      </div>

      {team.matches.slice(1).map(match => <MatchSmallCard key={match.id} teamName={team.teamName} match={match} />)}

      <div className="more-link">
        <Link to = {moreLinkRoute}>More..</Link>
      </div>

    </div>

    
  );
}
