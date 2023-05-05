import { Component, Input, OnDestroy, OnInit, Inject } from '@angular/core';
import { DynamicContentViewService } from './content/dynamic-content-view.service';
import { DynamicContentView } from './content/dynamic-content-view';
import {MatDialog, MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import { TenantSelectItem } from './domain/tenant-select-item';
import { MenuItemNode } from './domain/menu-item-node';


const TREE_DATA: MenuItemNode[] = [
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
  },
  {
    name: 'Tenant Management',
    children: [
      {
        name: 'Tenant Overview'
      },
      {
        name: 'Subscription'
      },
      {
        name: 'Billing'
      }
    ],
  },
];


@Component({
  selector: 'create-tenant-prompt-dialog',
  templateUrl: 'create-tenant-prompt-dialog.html',
})
export class CreateTenantPromptDialog {

  // @ts-ignore
  constructor(
    public dialogRef: MatDialogRef<DashboardComponent>,
  ) {}

  onDialogYes() {
    console.log('yes')
  }
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  panelOpenState = false;
  menuItemNodes = TREE_DATA;

  interval: number|undefined;
  @Input() views: { [id:string] : DynamicContentView } = {}
  @Input() view!: DynamicContentView;
  tenants: TenantSelectItem[] = [
    {value: 'tenant-1', viewValue: 'tenant-1'},
    {value: 'tenant-2', viewValue: 'tenant-2'},
    {value: 'tenant-3', viewValue: 'tenant-3'},
  ];

  constructor(private contentViewService: DynamicContentViewService, private dialog: MatDialog) {
  }
  ngOnInit(): void {
    this.views = this.contentViewService.getViews();
    this.view = this.views['system overview']
  }

  ngOnDestroy() {

    clearInterval(this.interval);
  }

  changeView(event: any) {
    console.log('change view request captured in dashboard')
    this.view = this.views['apis']
  }

  onClick(event: string) {
    console.log("on click: ", event.toLowerCase())
    this.view = this.views[event.toLowerCase()]
    console.log('interval: ', this.interval)
  }

  openDialog(): void {

    const dialogRef = this.dialog.open(CreateTenantPromptDialog, {
      data: {},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }




}

