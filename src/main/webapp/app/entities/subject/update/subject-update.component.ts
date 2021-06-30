import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISubject, Subject } from '../subject.model';
import { SubjectService } from '../service/subject.service';
import { IDegree } from 'app/entities/degree/degree.model';
import { DegreeService } from 'app/entities/degree/service/degree.service';

@Component({
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html',
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;

  degreesSharedCollection: IDegree[] = [];

  editForm = this.fb.group({
    id: [],
    nameMat: [],
    coefMat: [null, [Validators.min(1), Validators.max(5)]],
    degree: [null, Validators.required],
  });

  constructor(
    protected subjectService: SubjectService,
    protected degreeService: DegreeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      this.updateForm(subject);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subject = this.createFromForm();
    if (subject.id !== undefined) {
      this.subscribeToSaveResponse(this.subjectService.update(subject));
    } else {
      this.subscribeToSaveResponse(this.subjectService.create(subject));
    }
  }

  trackDegreeById(index: number, item: IDegree): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
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

  protected updateForm(subject: ISubject): void {
    this.editForm.patchValue({
      id: subject.id,
      nameMat: subject.nameMat,
      coefMat: subject.coefMat,
      degree: subject.degree,
    });

    this.degreesSharedCollection = this.degreeService.addDegreeToCollectionIfMissing(this.degreesSharedCollection, subject.degree);
  }

  protected loadRelationshipsOptions(): void {
    this.degreeService
      .query()
      .pipe(map((res: HttpResponse<IDegree[]>) => res.body ?? []))
      .pipe(map((degrees: IDegree[]) => this.degreeService.addDegreeToCollectionIfMissing(degrees, this.editForm.get('degree')!.value)))
      .subscribe((degrees: IDegree[]) => (this.degreesSharedCollection = degrees));
  }

  protected createFromForm(): ISubject {
    return {
      ...new Subject(),
      id: this.editForm.get(['id'])!.value,
      nameMat: this.editForm.get(['nameMat'])!.value,
      coefMat: this.editForm.get(['coefMat'])!.value,
      degree: this.editForm.get(['degree'])!.value,
    };
  }
}
