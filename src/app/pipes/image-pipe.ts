import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'imagePipe'
})
export class ImagePipe implements PipeTransform {
  PATH_TO_IMAGE = '/assets/images/weather_ico/';
  ICON_SUFFIX = '.png'

  transform(value: string | null): string | null {
    if (!value) {
      return null;
    }

    return this.PATH_TO_IMAGE + value + this.ICON_SUFFIX;
  }

}
