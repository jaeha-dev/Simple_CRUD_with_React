import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link, withRouter } from 'react-router-dom';
import { instanceOf } from 'prop-types';
import { withCookies, Cookies } from 'react-cookie';

class GroupList extends Component {
  
  // 
  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  // 생성자: 컴포넌트 생성 시, 가장 먼저 실행된다.
  constructor(props) {
    super(props);
    const {cookies} = props;
    this.state = {groups: [], csrfToken: cookies.get('XSRF-TOKEN'), isLoading: true};
    // 이벤트 바인딩
    this.remove = this.remove.bind(this);
  }

  // componentDidMount(): 컴포넌트 생성 후, 첫 렌더링이 완료되는 시점에 실행된다.
  componentDidMount() {
    this.setState({isLoading: true});

    // API 서버 호출
    fetch('/api/groups', {credentials: 'include'})
      .then(response => response.json())
      .then(data => this.setState({groups: data, isLoading: false}))
      .catch(() => this.props.history.push('/'));
  }

  // 그룹 삭제 이벤트
  async remove(id) {
    await fetch(`/api/groups/${id}`, {
      method: 'DELETE',
      headers: {
        'X-XSRF-TOKEN': this.state.csrfToken,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    }).then(() => {
      let updatedGroups = [...this.state.groups].filter(i => i.id !== id);
      this.setState({groups: updatedGroups});
    });
  }

  // 렌더링
  render() {
    const {groups, isLoading} = this.state;

    if (isLoading) {
      return <p>Loding ...</p>;
    }

    const groupList = groups.map(group => {
      const address = `${group.address || ''}, ${group.city || ''}, ${group.stateOrProvince || ''}`;
      
      return <tr key={group.id}>
        <td style={{whiteSpace: 'nowrap'}}>{group.name}</td>
        <td>{address}</td>
        <td>{group.events.map(event => {
          return <div key={event.id}>{new Intl.DateTimeFormat('en-US', {
            year: 'numeric',
            month: 'long',
            day: '2-digit'
          }).format(new Date(event.date))}: {event.title}</div>
        })}</td>
        
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/groups/" + group.id}>Edit</Button>
            {' '}
            <Button size="sm" color="danger" onClick={() => this.remove(group.id)}>Delete</Button>
          </ButtonGroup>
        </td>
      </tr>
    });

    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/groups/new">Create Group</Button>
          </div>

          <h3>My JUG Tour</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="25%">Name</th>
              <th width="25%">Location</th>
              <th width="25%">Events</th>
              <th width="25%">Actions</th>
            </tr>
            </thead>
            <tbody>
              {groupList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}

export default withCookies(withRouter(GroupList));