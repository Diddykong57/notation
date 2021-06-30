import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGet } from '../get.model';

@Component({
  selector: 'jhi-get-detail',
  templateUrl: './get-detail.component.html',
})
export class GetDetailComponent implements OnInit {
  get: IGet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ get }) => {
      this.get = get;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
