import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAssessment, Assessment } from '../assessment.model';
import { AssessmentService } from '../service/assessment.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

@Component({
  selector: 'jhi-assessment-update',
  templateUrl: './assessment-update.component.html',
})
export class AssessmentUpdateComponent implements OnInit {
  isSaving = false;

  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    coefCont: [null, [Validators.min(1), Validators.max(5)]],
    type: [],
    subject: [null, Validators.required],
  });

  constructor(
    protected assessmentService: AssessmentService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assessment }) => {
      this.updateForm(assessment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assessment = this.createFromForm();
    if (assessment.id !== undefined) {
      this.subscribeToSaveResponse(this.assessmentService.update(assessment));
    } else {
      this.subscribeToSaveResponse(this.assessmentService.create(assessment));
    }
  }

  trackSubjectById(index: number, item: ISubject): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssessment>>): void {
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

  protected updateForm(assessment: IAssessment): void {
    this.editForm.patchValue({
      id: assessment.id,
      date: assessment.date,
      coefCont: assessment.coefCont,
      type: assessment.type,
      subject: assessment.subject,
    });

    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(this.subjectsSharedCollection, assessment.subject);
  }

  protected loadRelationshipsOptions(): void {
    this.subjectService
      .query()
      .pipe(map((res: HttpResponse<ISubject[]>) => res.body ?? []))
      .pipe(
        map((subjects: ISubject[]) => this.subjectService.addSubjectToCollectionIfMissing(subjects, this.editForm.get('subject')!.value))
      )
      .subscribe((subjects: ISubject[]) => (this.subjectsSharedCollection = subjects));
  }

  protected createFromForm(): IAssessment {
    return {
      ...new Assessment(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      coefCont: this.editForm.get(['coefCont'])!.value,
      type: this.editForm.get(['type'])!.value,
      subject: this.editForm.get(['subject'])!.value,
    };
  }
}
