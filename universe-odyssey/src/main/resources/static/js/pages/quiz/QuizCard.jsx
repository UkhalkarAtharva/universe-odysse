import React from 'react';
import PropTypes from 'prop-types';
import '../../css/quiz.css';

const QuizCard = ({ question, index, onSelect, selected }) => {
  return (
    <article className={`mission-card fade-up`}>
      <div className="mission-card-content">
        <div className="mission-card-title">{index + 1}. {question.text}</div>
        {question.hint && <div className="mission-desc muted" style={{marginTop:6}}>{question.hint}</div>}
        <div className="quiz-options" style={{marginTop:12}}>
          {question.options.map((opt, i) => (
            <label className={`quiz-option ${selected === i ? 'selected' : ''}`} key={i}>
              <input type="radio" name={`q-${question.id}`} checked={selected === i} onChange={() => onSelect(question.id, i)} />
              <span className="option-text">{opt}</span>
            </label>
          ))}
        </div>
      </div>
    </article>
  );
};

QuizCard.propTypes = {
  question: PropTypes.object.isRequired,
  index: PropTypes.number,
  onSelect: PropTypes.func.isRequired,
  selected: PropTypes.number,
};

export default QuizCard;
import React from 'react';

const QuizCard = ({ question, index, onSelect, selected }) => {
  const handle = (idx) => onSelect(question.id, idx);
  return (
    <div className="mission-card fade-up" style={{marginBottom:12, animationDelay:`${Math.min(index,8)*80}ms`}}>
      <div className="mission-card-content">
        <h3 className="mission-card-title">{question.questionText}</h3>
        <div>
          {question.options && question.options.map((o, i) => (
            <label key={i} style={{display:'block', padding:'8px 0'}}>
              <input name={`q-${question.id}`} type="radio" checked={selected === i} onChange={() => handle(i)} />
              <span style={{marginLeft:8}}>{o}</span>
            </label>
          ))}
        </div>
      </div>
    </div>
  );
};

export default QuizCard;
