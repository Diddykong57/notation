import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDegree, Degree } from '../degree.model';
import { DegreeService } from '../service/degree.service';

@Component({
  selector: 'jhi-degree-update',
  templateUrl: './degree-update.component.html',
})
export class DegreeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nameDipl: [],
  });

  constructor(protected degreeService: DegreeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ degree }) => {
      this.updateForm(degree);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const degree = this.createFromForm();
    if (degree.id !== undefined) {
      this.subscribeToSaveResponse(this.degreeService.update(degree));
    } else {
      this.subscribeToSaveResponse(this.degreeService.create(degree));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDegree>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(degree: IDegree): void {
    this.editForm.patchValue({
      id: degree.id,
      nameDipl: degree.nameDipl,
    });
  }

  protected createFromForm(): IDegree {
    return {
      ...new Degree(),
      id: this.editForm.get(['id'])!.value,
      nameDipl: this.editForm.get(['nameDipl'])!.value,
    };
  }
}
