import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <MenuItem icon="asterisk" to="/researcher">
      <Translate contentKey="global.menu.entities.researcher" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/participant">
      <Translate contentKey="global.menu.entities.participant" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/form">
      <Translate contentKey="global.menu.entities.form" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/form-question">
      <Translate contentKey="global.menu.entities.formQuestion" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/i-give-2-user">
      <Translate contentKey="global.menu.entities.iGive2User" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/mobile-user">
      <Translate contentKey="global.menu.entities.mobileUser" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/participant-invitation">
      <Translate contentKey="global.menu.entities.participantInvitation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/form-answers">
      <Translate contentKey="global.menu.entities.formAnswers" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/answer">
      <Translate contentKey="global.menu.entities.answer" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/data">
      <Translate contentKey="global.menu.entities.data" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/participant-institution">
      <Translate contentKey="global.menu.entities.participantInstitution" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/study">
      <Translate contentKey="global.menu.entities.study" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/form-question">
      <Translate contentKey="global.menu.entities.formQuestion" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/mobile-user">
      <Translate contentKey="global.menu.entities.mobileUser" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
