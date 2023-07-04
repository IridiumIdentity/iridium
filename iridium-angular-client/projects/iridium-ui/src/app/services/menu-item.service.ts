import { Injectable } from '@angular/core';
import { MenuItemNode } from '../components/dashboard/domain/menu-item-node';

@Injectable({
  providedIn: 'root'
})
export class MenuItemService {

  private readonly menuItems: MenuItemNode[]
  constructor() {
    this.menuItems = [
      {
        name: 'Login Box',
        children: [
          {
            name: 'Login Box Settings'
          }
        ],
      },
      {
        name: 'Application Management',
        children: [
          {
            name: 'Applications'
          }
        ],
      },
      {
        name: 'User Management',
        children: [
          {
            name: 'Users'
          }
        ],
      }
    ];
  }

  getMenuItems(): MenuItemNode[] {
    return this.menuItems;
  }
}
