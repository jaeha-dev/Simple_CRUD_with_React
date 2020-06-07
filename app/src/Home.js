import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';
import { withCookies } from 'react-cookie';

class Home extends Component {
  
  // Home 컴포넌트 내부에서 사용할 데이터 (값 변경 가능)
  state = {
    isLoading: true,
    isAuthenticated: false,
    user: undefined
  };

  // 생성자: 컴포넌트 생성 시, 가장 먼저 실행된다.
  // Spring Security의 CSRF 쿠키는 다시 보내야 할 헤더와 이름이 다르다.
  // (쿠키 이름: XSRF-TOKEN)
  constructor(props) {
    super(props);
    const {cookies} = props;
    this.state.csrfToken = cookies.get('XSRF-TOKEN');
    // 이벤트 바인딩
    this.login = this.login.bind(this);
    this.logout = this.logout.bind(this);
  }

  // componentDidMount(): 컴포넌트 생성 후, 첫 렌더링이 완료되는 시점에 실행된다.
  async componentDidMount() {
    const response = await fetch('/api/user', {credentials: 'include'});
    const body = await response.text();

    if (body === '') {
      this.setState(({isAuthenticated: false}));
    } else {
      this.setState(({isAuthenticated: true, user: JSON.parse(body)}));
    }
  }

  // 로그인 이벤트
  login() {
    let port = (window.location.port ? ':' + window.location.port : '');
    if (port === ':3000') {
      port = ':8080';
    }

    window.location.href = '//' + window.location.hostname + port + '/private';
    // console.log(window.location.href); // http://localhost:3000/
  }

  // 로그아웃 이벤트
  // Spring Security의 CSRF 쿠키는 다시 보내야 할 헤더와 이름이 다르다.
  // (헤더 이름: X-XSRF-TOKEN)
  logout() {
    fetch('/api/logout', {
      method: 'POST', 
      credentials: 'include',
      headers: {'X-XSRF-TOKEN': this.state.csrfToken}})

      .then(res => res.json())
      .then(response => {
        window.location.href = response.logoutUrl + "?id_token_hint=" +
          response.idToken + "&post_logout_redirect_uri=" + window.location.origin;
      }
    );
  }

  // 렌더링
  render() {
    const message = this.state.user ? <h2>Welcome, {this.state.user.name}!</h2>
                                    : <p>Login is required.</p>;
    const button = this.state.isAuthenticated ? 
    <div>
      <Button color="link"><Link to="/groups">Manage JUG Tour</Link></Button>
      <br/>
      <Button color="link" onClick={this.logout}>Logout</Button>
    </div> : 
    <Button color="link" onClick={this.login}>Login</Button>;

    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          {message}
          {button}
        </Container>
      </div>
    );
  }
}

// withCookies(Home): 컴포넌트를 감싸서 쿠키를 활용할 수 있도록 한다.
export default withCookies(Home);