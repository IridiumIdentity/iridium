import { Injectable } from '@angular/core';

import { DynamicContentView } from './dynamic-content-view';
import { UserOverviewComponent } from './user-overview/user-overview.component';
import { RolesOverviewComponent } from './roles-overview/roles-overview.component';
import { ApplicationOverviewComponent } from './application-overview/application-overview.component';
import { ApiOverviewComponent } from './api-overview/api-overview.component';
import { SystemOverviewComponent } from './system-overview/system-overview.component';
import { TenantOverviewComponent } from './tenant-overview/tenant-overview.component';
import { LoginBoxOverviewComponent } from './login-box-overview/login-box-overview.component';

@Injectable()
export class DynamicContentViewService {
  viewDictionary: { [id:string] : DynamicContentView } = {}

  constructor() {

    this.viewDictionary['applications'] = new DynamicContentView(
      ApplicationOverviewComponent, {}
    )
    this.viewDictionary['apis'] = new DynamicContentView(
      ApiOverviewComponent, {}
    )

    this.viewDictionary['users'] = new DynamicContentView(
      UserOverviewComponent, {}
    )

    this.viewDictionary['roles'] = new DynamicContentView(
      RolesOverviewComponent, {}
    )

    this.viewDictionary['system overview'] = new DynamicContentView(
      SystemOverviewComponent, {}
    )

    this.viewDictionary['tenant overview'] = new DynamicContentView(
      TenantOverviewComponent, {}
    )

    this.viewDictionary['login box settings'] = new DynamicContentView(
      LoginBoxOverviewComponent, {}
    )
  }



  getViews() {
    return this.viewDictionary
  }

  getViewsForTenant(tenantId: string) {
    for(let key in this.viewDictionary) {
      this.viewDictionary[key].data.tenantId = tenantId
    }
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
