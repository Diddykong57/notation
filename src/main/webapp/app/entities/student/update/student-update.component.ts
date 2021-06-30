import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { IDegree } from 'app/entities/degree/degree.model';
import { DegreeService } from 'app/entities/degree/service/degree.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  degreesSharedCollection: IDegree[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    degree: [null, Validators.required],
  });

  constructor(
    protected studentService: StudentService,
    protected degreeService: DegreeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackDegreeById(index: number, item: IDegree): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      firstName: student.firstName,
      lastName: student.lastName,
      degree: student.degree,
    });

    this.degreesSharedCollection = this.degreeService.addDegreeToCollectionIfMissing(this.degreesSharedCollection, student.degree);
  }

  protected loadRelationshipsOptions(): void {
    this.degreeService
      .query()
      .pipe(map((res: HttpResponse<IDegree[]>) => res.body ?? []))
      .pipe(map((degrees: IDegree[]) => this.degreeService.addDegreeToCollectionIfMissing(degrees, this.editForm.get('degree')!.value)))
      .subscribe((degrees: IDegree[]) => (this.degreesSharedCollection = degrees));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      degree: this.editForm.get(['degree'])!.value,
    };
  }
}
