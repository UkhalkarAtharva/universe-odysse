import React, { useEffect, useState } from 'react';
import api from '../../api';
import LeaderboardItem from './LeaderboardItem';
import '../../css/leaderboard.css';

const Leaderboard = () => {
  const [leaders, setLeaders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');
  const [page, setPage] = useState(1);
  const pageSize = 20;
  const [currentUserId, setCurrentUserId] = useState(null);
  useEffect(() => {
    let mounted = true;
    const fetch = async () => {
      try {
        const r = await api.get('/leaderboard');
        if (mounted) setLeaders(r.data || []);
        try { const me = await api.get('/auth/me'); if(mounted) setCurrentUserId(me.data.id); } catch (e) { /* ignore */ }
      } catch (err) { 
        if (err && err.response && err.response.status === 401) { window.location.href = '/login.html'; return; }
      }
      finally { if(mounted) setLoading(false); }
    };
    fetch();
    return () => { mounted = false; };
  },[]);
  if (loading) return <div className="missions-loading">Loading...</div>;
  return (
    <div className="container">
      <div className="ui-hero"><h1 className="title">Leaderboard</h1></div>
      <div className="leaderboard-container">
        <div className="leaderboard-search">
          <input placeholder="Search by username" value={filter} onChange={(e)=>{setFilter(e.target.value); setPage(1);}} />
        </div>
        <div className="leaderboard-list">
          {leaders.filter(x => !filter || x.username.toLowerCase().includes(filter.toLowerCase())).slice((page-1)*pageSize, page*pageSize).map((l, idx) => (
            <LeaderboardItem key={l.userId} item={l} rank={(page-1)*pageSize + idx + 1} currentUserId={currentUserId} />
          ))}
        </div>
        <div style={{display:'flex',gap:8, justifyContent:'center', marginTop:12}}>
          <button className="cta-btn" disabled={page===1} onClick={()=>setPage(p=>Math.max(1,p-1))}>Prev</button>
          <div style={{padding:8}}>{page} / {Math.ceil((leaders.filter(x=>!filter || x.username.toLowerCase().includes(filter.toLowerCase())).length||0)/pageSize)}</div>
          <button className="cta-btn" disabled={page*pageSize >= (leaders.filter(x=>!filter || x.username.toLowerCase().includes(filter.toLowerCase())).length||0)} onClick={()=>setPage(p=>p+1)}>Next</button>
        </div>
      </div>
    </div>
  );
};

export default Leaderboard;
