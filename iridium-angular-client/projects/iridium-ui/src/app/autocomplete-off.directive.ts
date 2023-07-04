import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appAutocompleteOff]'
})
export class AutocompleteOffDirective {

  constructor(private _el: ElementRef) {
    this._el.nativeElement.setAttribute('autocomplete', 'off');
    this._el.nativeElement.setAttribute('autocorrect', 'off');
    this._el.nativeElement.setAttribute('autocapitalize', 'none');
    this._el.nativeElement.setAttribute('spellcheck', 'false');
  }

}
