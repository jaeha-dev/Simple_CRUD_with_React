import React, { Component } from 'react';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink } from 'reactstrap';
import { Link } from 'react-router-dom';

export default class AppNavbar extends Component {
  
  // 생성자: 컴포넌트 생성 시, 가장 먼저 실행된다.
  constructor(props) {
    super(props);
    this.state = {isOpen: false};
    // 이벤트 바인딩
    this.toggle = this.toggle.bind(this);
  }

  // 토글 이벤트
  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  // 렌더링
  render() {
    return <Navbar color="dark" dark expand="md">
      <NavbarBrand tag={Link} to="/">Home</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>

      <Collapse isOpen={this.state.isOpen} navbar>
        <Nav className="ml-auto" navbar>
          <NavItem>
            <NavLink href="jaeha.dev@gmail.com">@Email</NavLink>
          </NavItem>
          <NavItem>
          <NavLink href="https://github.com/jaeha-dev/simple-crud-with-react">@GitHub</NavLink>
          </NavItem>
        </Nav>
      </Collapse>
    </Navbar>
  }
}