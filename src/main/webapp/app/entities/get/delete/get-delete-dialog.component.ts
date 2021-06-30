import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGet } from '../get.model';
import { GetService } from '../service/get.service';

@Component({
  templateUrl: './get-delete-dialog.component.html',
})
export class GetDeleteDialogComponent {
  get?: IGet;

  constructor(protected getService: GetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.getService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
