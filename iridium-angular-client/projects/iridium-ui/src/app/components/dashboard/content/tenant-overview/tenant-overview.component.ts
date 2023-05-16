import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';

@Component({
  selector: 'app-tenant-overview',
  templateUrl: './tenant-overview.component.html',
  styleUrls: ['./tenant-overview.component.css']
})
export class TenantOverviewComponent implements DynamicContentViewItem {
  @Input() data: any;
  constructor() { }



}
