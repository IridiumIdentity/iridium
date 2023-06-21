import { Component, Input, OnInit, } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { CookieService } from '../../../../services/cookie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityService } from '../../../../services/identity.service';
import { IdentitySummaryResponse } from '../../domain/identity-summary-response';

type IdentitySummaryMapType = {
  [id: string]: IdentitySummaryResponse;
}
@Component({
  selector: 'app-user-overview',
  templateUrl: './user-overview.component.html',
  styleUrls: ['./user-overview.component.css']
})
export class UserOverviewComponent implements DynamicContentViewItem, OnInit {
  @Input() data: any;
  displayedColumns: string[] = ['id', 'emailAddress'];
  dataSource: IdentitySummaryResponse[] = [];

  constructor(private identityServie: IdentityService, private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) { }

  onRowClick(index: number) {
    console.log('clicked on row: ', index)
  }

  ngOnInit(): void {
    this.refreshDataSource();

  }

  private refreshDataSource() {
    this.dataSource = [];
    this.identityServie.getSummariesByParentTenant(this.data.tenantId, 100)
      .subscribe(identitySummaries => {
        for (let i = 0; i < identitySummaries.data.length; i++) {
          let summary = identitySummaries.data[i];
          const newRow = {
            id: summary.id,
            emailAddress: summary.emailAddress,
          };
          this.dataSource = [...this.dataSource, newRow]
        }

      })
  }
}
