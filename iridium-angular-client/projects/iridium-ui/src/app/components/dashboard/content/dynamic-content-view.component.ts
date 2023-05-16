import { Component, Input, OnDestroy, OnInit, Type, ViewChild } from '@angular/core';

import { DynamicContentViewDirective } from './dynamic-content-view.directive';
import { DynamicContentView } from './dynamic-content-view';
import { DynamicContentViewItem } from './dynamic-content-view-item';


@Component({
  selector: 'app-dynamic-content-view',
  templateUrl: './dynamic-content-view.component.html',
  styleUrls: ['./dynamic-content-view.component.css']
})
export class DynamicContentViewComponent implements OnInit, OnDestroy {
  @Input() views: { [id:string] : DynamicContentView } = {};

  @Input() view!: DynamicContentView;

  @Input() tenantId!: string;

  previousComponent!: Type<any>;

  @ViewChild(DynamicContentViewDirective, {static: true}) dynamicContentViewHost!: DynamicContentViewDirective;

  private clearTimer: VoidFunction | undefined;

  ngOnInit(): void {
    this.getDynamicViews();
  }

  ngOnDestroy() {
    this.clearTimer?.();
  }

  loadSingleComponent() {
    if (this.view.component == this.previousComponent) {
      return;
    }
    const viewContainerRef = this.dynamicContentViewHost.viewContainerRef;
    viewContainerRef.clear();
    this.previousComponent = this.view.component
    const componentRef = viewContainerRef.createComponent<DynamicContentViewItem>(this.view.component);
    componentRef.instance.data = this.view.data;
  }

  getDynamicViews() {
    const interval = setInterval(() => {
      //this.loadDynamicComponent();
      this.loadSingleComponent()
    }, 0);
    this.clearTimer = () => clearInterval(interval);
  }
}


/*
Copyright Google LLC. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at https://angular.io/license
*/
