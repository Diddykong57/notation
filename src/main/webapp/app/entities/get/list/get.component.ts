import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGet } from '../get.model';
import { GetService } from '../service/get.service';
import { GetDeleteDialogComponent } from '../delete/get-delete-dialog.component';

@Component({
  selector: 'jhi-get',
  templateUrl: './get.component.html',
})
export class GetComponent implements OnInit {
  gets?: IGet[];
  isLoading = false;

  constructor(protected getService: GetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.getService.query().subscribe(
      (res: HttpResponse<IGet[]>) => {
        this.isLoading = false;
        this.gets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGet): number {
    return item.id!;
  }

  delete(get: IGet): void {
    const modalRef = this.modalService.open(GetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.get = get;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
