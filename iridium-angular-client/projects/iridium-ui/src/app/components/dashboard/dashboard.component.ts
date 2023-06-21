import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { DynamicContentViewService } from './content/dynamic-content-view.service';
import { DynamicContentView } from './content/dynamic-content-view';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import { TenantSelectItem } from './domain/tenant-select-item';
import { NgxIridiumClientService } from '../../../../../ngx-iridium-client/src/lib/ngx-iridium-client.service';
import { TenantService } from '../../services/tenant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSelectChange } from '@angular/material/select';
import { MenuItemNode } from './domain/menu-item-node';
import { MenuItemService } from '../../services/menu-item.service';
import { Router } from '@angular/router';





@Component({
  selector: 'create-tenant-prompt-dialog',
  templateUrl: 'create-tenant-prompt-dialog.html',
})
export class CreateTenantPromptDialog {
  createTenantFormGroup: FormGroup;
  constructor(
    private dialogRef: MatDialogRef<DashboardComponent>,
    private tenantService: TenantService,
    private formBuilder: FormBuilder
  ) {
    console.log("dashboard constructed")
    this.createTenantFormGroup = this.formBuilder.group({
      tenantName: ['', Validators.required],
      environment: ['', Validators.required],
    });

  }


  onDialogYes() {
    const dialogRef = this.dialogRef;
    this.tenantService.create(this.createTenantFormGroup)
      .subscribe({
        next(v) {
          console.log('success creating tenant')
          console.log(v)
          dialogRef.close({})

        },
       error(e) {
          console.log('error creating tenant')
         console.log(e)
       },
        complete() {
          console.log('complete')
        }
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
  menuItemNodes: MenuItemNode[];
  interval: number|undefined;
  @Input() views: { [id:string] : DynamicContentView } = {}
  @Input() view!: DynamicContentView;
  tenants: TenantSelectItem[] = [];
  selectedTenant!: string;

  constructor(private router: Router, private contentViewService: DynamicContentViewService, private dialog: MatDialog, private iridiumClient: NgxIridiumClientService, private tenantService: TenantService, private menuItemService: MenuItemService) {
    this.menuItemNodes = this.menuItemService.getMenuItems();
  }
  ngOnInit(): void {
    this.views = this.contentViewService.getViews();
    this.view = this.views['system overview']

    this.getTenantSummaries();

  }

  getTenantSummaries() {
    this.tenants = [];
    this.tenantService.getTenantSummaries()
      .subscribe(summaries => {
        console.log('summaries', summaries)
        for (let i = 0; i < summaries.length; i++) {
          this.tenants.push({ value: summaries[i].id, viewValue: summaries[i].subdomain})
        }


      })
  }

  onTenantChange(event: MatSelectChange) {
    this.selectedTenant = event.value;
    for(let key in this.views) {
      this.views = this.contentViewService.getViewsForTenant(this.selectedTenant)
      this.view = this.contentViewService.getView('system overview')
    }
  }

  ngOnDestroy() {

    clearInterval(this.interval);
  }

  subItemClick(event: string) {
    this.view = this.views[event.toLowerCase()]
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(CreateTenantPromptDialog, {
      data: {},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('get tenant summaries')
      this.getTenantSummaries()
    });

  }

  login() {
    this.iridiumClient.authenticateWithExternalRedirect();

  }

  register() {
    this.router.navigateByUrl('/register');
  }

  homeButtonClick(): void {
    this.router.navigateByUrl('/');
  }
}

