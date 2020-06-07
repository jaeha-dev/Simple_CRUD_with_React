import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import GroupList from './GroupList';
import GroupEdit from './GroupEdit';
import { CookiesProvider } from 'react-cookie';

class App extends Component {

  // 렌더링
  render() {
    return (
      // CookiesProvider: CSRF 쿠키를 읽고 이를 다시 헤더로 보낼 수 있다.
      <CookiesProvider>
        <Router>
          <Switch>
            {/* exact 또는 exact={true}의 의미는 주어진 경로와 정확히 일치하는 경우에만 컴포넌트를 그린다. */}
            <Route path='/' exact={true} component={Home}/>
            <Route path='/groups' exact={true} component={GroupList}/>
            {/* 콜론(:)은 경로 매개변수(Route Parameter)를 명시하는 것이다. */}
            <Route path='/groups/:id' component={GroupEdit}/>
          </Switch>
        </Router>
      </CookiesProvider>
    );
  }
}

export default App;