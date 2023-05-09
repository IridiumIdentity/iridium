import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { DynamicContentViewService } from './content/dynamic-content-view.service';
import { DynamicContentView } from './content/dynamic-content-view';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import { TenantSelectItem } from './domain/tenant-select-item';
import { MenuItemNode } from './domain/menu-item-node';
import { NgxIridiumClientService } from '../../../../../ngx-iridium-client/src/lib/ngx-iridium-client.service';
import { TenantService } from '../../services/tenant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSelectChange } from '@angular/material/select';


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
  createTenantFormGroup: FormGroup;
  // @ts-ignore
  constructor(
    public dialogRef: MatDialogRef<DashboardComponent>,
    private tenantService: TenantService,
    private formBuilder: FormBuilder
  ) {
    this.createTenantFormGroup = this.formBuilder.group({
      tenantName: ['', Validators.required],
      environment: ['', Validators.required],
    });
  }

  onDialogYes() {
    console.log('yes')
    this.tenantService.create(this.createTenantFormGroup)
      .subscribe(response => {
        console.log('success response is: ', response)
          window.location.reload();
      })
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
  tenants: TenantSelectItem[] = [];
  selectedTenant!: string;

  constructor(private contentViewService: DynamicContentViewService, private dialog: MatDialog, private iridiumClient: NgxIridiumClientService, private tenantService: TenantService) {
  }
  ngOnInit(): void {
    this.iridiumClient.authorize();
    this.views = this.contentViewService.getViews();
    this.view = this.views['system overview']
    this.getTenantSummaries();

  }

  getTenantSummaries() {
    this.tenantService.getTenantSummaries()
      .subscribe(summaries => {
        for (let i = 0; i < summaries.length; i++) {
          this.tenants.push({ value: summaries[i].id, viewValue: summaries[i].subdomain})
        }
      })
  }

  onTenantChange(event: MatSelectChange) {
    this.selectedTenant = event.value;
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
    this.dialog.open(CreateTenantPromptDialog, {
      data: {},
    });
  }
}

