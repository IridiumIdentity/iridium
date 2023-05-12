import { Component, Input } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';

@Component({
  selector: 'app-system-overview',
  templateUrl: './system-overview.component.html',
  styleUrls: ['./system-overview.component.css']
})
export class SystemOverviewComponent implements DynamicContentViewItem {

  @Input() data: any;
  constructor() {
  }


}
