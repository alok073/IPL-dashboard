import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { MatchDetailCard } from '../components/MatchDetailCard';
import { YearSelector } from '../components/YearSelector';

import './MatchPage.scss';

export const MatchPage = () => {

  const [matches, setMatches] = useState([]);
  const { teamName, year } = useParams();

  /**
   * Making a REST API call
   */
   useEffect(
    () => {
      const fetchMatches = async () => {
        const response = await fetch(`${process.env.REACT_APP_API_ROOT_URL}/team/${teamName}/matches?year=${year}`);
        const data = await response.json();
        //console.log(data);
        //setting the data field in to the state of the component
        setMatches(data);
      };

      fetchMatches();
    }, [teamName, year]
  );


  return (
    <div className="MatchPage" >
      <div className="year-selector">
        <h4>Select Year</h4>
        <YearSelector teamName={teamName} />
      </div>
      <div>
      <h2 className = "page-heading">{teamName} matches in the year {year}</h2>
        {
          matches.map(match => <MatchDetailCard key={match.id} teamName = {teamName} match={match} />)
        }
      </div>
      
    </div>
  );
}
