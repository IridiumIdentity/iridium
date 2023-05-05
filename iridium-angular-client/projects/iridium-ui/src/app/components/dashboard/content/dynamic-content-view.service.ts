import { Injectable } from '@angular/core';

import { DynamicContentView } from './dynamic-content-view';
import { UserOverviewComponent } from './user-overview/user-overview.component';
import { RolesOverviewComponent } from './roles-overview/roles-overview.component';
import { ApplicationOverviewComponent } from './application-overview/application-overview.component';
import { ApiOverviewComponent } from './api-overview/api-overview.component';
import { SystemOverviewComponent } from './system-overview/system-overview.component';
import { TenantOverviewComponent } from './tenant-overview/tenant-overview.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { BillingComponent } from './billing/billing.component';

@Injectable()
export class DynamicContentViewService {
  viewDictionary: { [id:string] : DynamicContentView } = {}

  constructor() {

    this.viewDictionary['applications'] = new DynamicContentView(
      ApplicationOverviewComponent,
      { name: 'Applications', bio: 'manage them here' }
    )
    this.viewDictionary['apis'] = new DynamicContentView(
      ApiOverviewComponent,
      { name: 'APIS', bio: 'manage APIS' }
    )

    this.viewDictionary['users'] = new DynamicContentView(
      UserOverviewComponent,
      { headline: 'Users', body: 'Look at your users' }
    )

    this.viewDictionary['roles'] = new DynamicContentView(
      RolesOverviewComponent,
      { headline: 'Roles', body: 'Define roles in your application' }
    )

    this.viewDictionary['system overview'] = new DynamicContentView(
      SystemOverviewComponent,
      { headline: 'Roles', body: 'Define roles in your application' }
    )

    this.viewDictionary['tenant overview'] = new DynamicContentView(
      TenantOverviewComponent,
      { headline: 'Roles', body: 'Define roles in your application' }
    )

    this.viewDictionary['subscription'] = new DynamicContentView(
      SubscriptionComponent,
      { headline: 'Roles', body: 'Define roles in your application' }
    )

    this.viewDictionary['billing'] = new DynamicContentView(
      BillingComponent,
      { headline: 'Roles', body: 'Define roles in your application' }
    )
  }



  getViews() {
    return this.viewDictionary
  }

  getView(key: string) {
    return this.viewDictionary[key]
  }


}


/*
Copyright Google LLC. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at https://angular.io/license
*/
