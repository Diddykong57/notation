import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDegree } from '../degree.model';

@Component({
  selector: 'jhi-degree-detail',
  templateUrl: './degree-detail.component.html',
})
export class DegreeDetailComponent implements OnInit {
  degree: IDegree | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ degree }) => {
      this.degree = degree;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
