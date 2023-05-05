import { Component, Input, OnInit } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';

@Component({
  selector: 'app-billing',
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.css']
})
export class BillingComponent implements DynamicContentViewItem {
  @Input() data: any;
  constructor() { }


}
