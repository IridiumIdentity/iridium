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
        name: 'System Metrics',
        children: [
          {
            name: 'System Overview'
          },

        ],
      },
      {
        name: 'Application Management',
        children: [
          {
            name: 'Applications'
          },
          {
            name: 'APIs'
          }
        ],
      },
      {
        name: 'User Management',
        children: [
          {
            name: 'Users'
          },
          {
            name: 'Roles'
          },
        ],
      }
    ];
  }

  getMenuItems(): MenuItemNode[] {
    return this.menuItems;
  }
}
