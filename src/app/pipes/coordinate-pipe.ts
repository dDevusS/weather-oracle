import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'coordinate'
})
export class CoordinatePipe implements PipeTransform {

  transform(value: number | null): string | null {
    if (!value) {
      return null;
    }

    return value.toFixed(4);
  }

}
