import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StateGeneratorService {

  constructor() { }

  generate(): string {
    return this.utf8ToHex(crypto.randomUUID());
  }

  private utf8ToHex(str: string) {
    return Array.from(str).map(c =>
      c.charCodeAt(0) < 128 ? c.charCodeAt(0).toString(16) :
        encodeURIComponent(c).replace(/\%/g, '').toLowerCase()
    ).join('');
  }
}
