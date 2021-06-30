import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGet, Get } from '../get.model';
import { GetService } from '../service/get.service';
import { IAssessment } from 'app/entities/assessment/assessment.model';
import { AssessmentService } from 'app/entities/assessment/service/assessment.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-get-update',
  templateUrl: './get-update.component.html',
})
export class GetUpdateComponent implements OnInit {
  isSaving = false;

  assessmentsSharedCollection: IAssessment[] = [];
  studentsSharedCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    note: [null, [Validators.required, Validators.min(0), Validators.max(20)]],
    assessment: [null, Validators.required],
    student: [null, Validators.required],
  });

  constructor(
    protected getService: GetService,
    protected assessmentService: AssessmentService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ get }) => {
      this.updateForm(get);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const get = this.createFromForm();
    if (get.id !== undefined) {
      this.subscribeToSaveResponse(this.getService.update(get));
    } else {
      this.subscribeToSaveResponse(this.getService.create(get));
    }
  }

  trackAssessmentById(index: number, item: IAssessment): number {
    return item.id!;
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGet>>): void {
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

  protected updateForm(get: IGet): void {
    this.editForm.patchValue({
      id: get.id,
      note: get.note,
      assessment: get.assessment,
      student: get.student,
    });

    this.assessmentsSharedCollection = this.assessmentService.addAssessmentToCollectionIfMissing(
      this.assessmentsSharedCollection,
      get.assessment
    );
    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsSharedCollection, get.student);
  }

  protected loadRelationshipsOptions(): void {
    this.assessmentService
      .query()
      .pipe(map((res: HttpResponse<IAssessment[]>) => res.body ?? []))
      .pipe(
        map((assessments: IAssessment[]) =>
          this.assessmentService.addAssessmentToCollectionIfMissing(assessments, this.editForm.get('assessment')!.value)
        )
      )
      .subscribe((assessments: IAssessment[]) => (this.assessmentsSharedCollection = assessments));

    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));
  }

  protected createFromForm(): IGet {
    return {
      ...new Get(),
      id: this.editForm.get(['id'])!.value,
      note: this.editForm.get(['note'])!.value,
      assessment: this.editForm.get(['assessment'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
