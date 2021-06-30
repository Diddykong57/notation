import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDegree } from '../degree.model';
import { DegreeService } from '../service/degree.service';

@Component({
  templateUrl: './degree-delete-dialog.component.html',
})
export class DegreeDeleteDialogComponent {
  degree?: IDegree;

  constructor(protected degreeService: DegreeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.degreeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
