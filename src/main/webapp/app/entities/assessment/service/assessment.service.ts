import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssessment, getAssessmentIdentifier } from '../assessment.model';

export type EntityResponseType = HttpResponse<IAssessment>;
export type EntityArrayResponseType = HttpResponse<IAssessment[]>;

@Injectable({ providedIn: 'root' })
export class AssessmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assessments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assessment: IAssessment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assessment);
    return this.http
      .post<IAssessment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assessment: IAssessment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assessment);
    return this.http
      .put<IAssessment>(`${this.resourceUrl}/${getAssessmentIdentifier(assessment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assessment: IAssessment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assessment);
    return this.http
      .patch<IAssessment>(`${this.resourceUrl}/${getAssessmentIdentifier(assessment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssessment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssessment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssessmentToCollectionIfMissing(
    assessmentCollection: IAssessment[],
    ...assessmentsToCheck: (IAssessment | null | undefined)[]
  ): IAssessment[] {
    const assessments: IAssessment[] = assessmentsToCheck.filter(isPresent);
    if (assessments.length > 0) {
      const assessmentCollectionIdentifiers = assessmentCollection.map(assessmentItem => getAssessmentIdentifier(assessmentItem)!);
      const assessmentsToAdd = assessments.filter(assessmentItem => {
        const assessmentIdentifier = getAssessmentIdentifier(assessmentItem);
        if (assessmentIdentifier == null || assessmentCollectionIdentifiers.includes(assessmentIdentifier)) {
          return false;
        }
        assessmentCollectionIdentifiers.push(assessmentIdentifier);
        return true;
      });
      return [...assessmentsToAdd, ...assessmentCollection];
    }
    return assessmentCollection;
  }

  protected convertDateFromClient(assessment: IAssessment): IAssessment {
    return Object.assign({}, assessment, {
      date: assessment.date?.isValid() ? assessment.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assessment: IAssessment) => {
        assessment.date = assessment.date ? dayjs(assessment.date) : undefined;
      });
    }
    return res;
  }
}
