import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDegree } from '../degree.model';
import { DegreeService } from '../service/degree.service';
import { DegreeDeleteDialogComponent } from '../delete/degree-delete-dialog.component';

@Component({
  selector: 'jhi-degree',
  templateUrl: './degree.component.html',
})
export class DegreeComponent implements OnInit {
  degrees?: IDegree[];
  isLoading = false;

  constructor(protected degreeService: DegreeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.degreeService.query().subscribe(
      (res: HttpResponse<IDegree[]>) => {
        this.isLoading = false;
        this.degrees = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDegree): number {
    return item.id!;
  }

  delete(degree: IDegree): void {
    const modalRef = this.modalService.open(DegreeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.degree = degree;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
