import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-dialog',
  imports: [
    MatIcon
  ],
  templateUrl: './dialog.html',
  styleUrl: './dialog.css'
})
export class Dialog {
  constructor(
    public dialogRef: MatDialogRef<Dialog>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title: string;
      message: string;
      confirmText: string;
      cancelText: string;
    }
  ) {}

}
