import { Injectable } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {Dialog} from './dialog';

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  constructor(private dialog: MatDialog) {}

  openConfirmDialog(config: {
    title: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
  }) {
    return this.dialog.open(Dialog, {
      data: config,
      panelClass: 'custom-dialog-container',
      autoFocus: false
    }).afterClosed();
  }
}
