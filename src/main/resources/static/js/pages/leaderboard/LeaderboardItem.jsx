import React from 'react';
import PropTypes from 'prop-types';
import '../../css/leaderboard.css';

const LeaderboardItem = ({ item, rank, currentUserId }) => {
  const isCurrent = currentUserId && currentUserId === item.userId;
  const medal = rank === 1 ? '🥇' : rank === 2 ? '🥈' : rank === 3 ? '🥉' : null;
  return (
    <div className={`leaderboard-item ${isCurrent ? 'highlight' : ''}`}>
      <div className="leaderboard-rank">{medal || rank}</div>
      <div className="leaderboard-user">{item.username}</div>
      <div className="leaderboard-points">{item.points}</div>
    </div>
  );
};

LeaderboardItem.propTypes = {
  item: PropTypes.object.isRequired,
  rank: PropTypes.number.isRequired,
  currentUserId: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};

export default LeaderboardItem;
import React from 'react';

const tierBadgeClass = (tier) => {
  switch ((tier||'').toLowerCase()) {
    case 'radiant': return 'badge-radiant';
    case 'immortal': return 'badge-immortal';
    case 'guardian': return 'badge-guardian';
    default: return 'badge-participant';
  }
};

const medalForRank = (rank) => rank === 1 ? '🥇' : rank === 2 ? '🥈' : rank === 3 ? '🥉' : null;

const LeaderboardItem = ({ item, currentUserId }) => {
  const highlight = currentUserId === item.userId;
  const medal = medalForRank(item.rank);
  return (
    <div className={`leaderboard-item ${highlight ? 'highlight' : ''}`}>
      <div className="leaderboard-rank">{medal ? <span className="medal">{medal}</span> : item.rank}</div>
      <div className="leaderboard-user">
        <div style={{display: 'flex', alignItems: 'center', gap:10}}>
          <div style={{fontWeight:700}}>{item.username}</div>
          <div className={tierBadgeClass(item.tier)}>{item.tier || 'Participant'}</div>
        </div>
        <div style={{color:'#9fb0cc'}}>{item.userId}</div>
      </div>
      <div className="leaderboard-points">{item.totalPoints}</div>
    </div>
  );
};

export default LeaderboardItem;
