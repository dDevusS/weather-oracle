import {Component, Input, input} from '@angular/core';

@Component({
  selector: 'svg[icon]',
  imports: [],
  template: '<svg:use [attr.href]="href"></svg:use>',
  styles: ['']
})
export class Svg {
  @Input() icon: string = '';

  get href() {
    return `/assets/svg/${this.icon}.svg#${this.icon}`;
  }
}
