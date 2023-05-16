import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[dynamicContentViewHost]',
})
export class DynamicContentViewDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}



/*
Copyright Google LLC. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at https://angular.io/license
*/
