import { Component, Input, OnInit } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.css']
})
export class SubscriptionComponent implements DynamicContentViewItem {
  @Input() data: any;
  constructor() { }


}
