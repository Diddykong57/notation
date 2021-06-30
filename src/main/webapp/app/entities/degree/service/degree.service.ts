import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDegree, getDegreeIdentifier } from '../degree.model';

export type EntityResponseType = HttpResponse<IDegree>;
export type EntityArrayResponseType = HttpResponse<IDegree[]>;

@Injectable({ providedIn: 'root' })
export class DegreeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/degrees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(degree: IDegree): Observable<EntityResponseType> {
    return this.http.post<IDegree>(this.resourceUrl, degree, { observe: 'response' });
  }

  update(degree: IDegree): Observable<EntityResponseType> {
    return this.http.put<IDegree>(`${this.resourceUrl}/${getDegreeIdentifier(degree) as number}`, degree, { observe: 'response' });
  }

  partialUpdate(degree: IDegree): Observable<EntityResponseType> {
    return this.http.patch<IDegree>(`${this.resourceUrl}/${getDegreeIdentifier(degree) as number}`, degree, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDegree>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDegree[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDegreeToCollectionIfMissing(degreeCollection: IDegree[], ...degreesToCheck: (IDegree | null | undefined)[]): IDegree[] {
    const degrees: IDegree[] = degreesToCheck.filter(isPresent);
    if (degrees.length > 0) {
      const degreeCollectionIdentifiers = degreeCollection.map(degreeItem => getDegreeIdentifier(degreeItem)!);
      const degreesToAdd = degrees.filter(degreeItem => {
        const degreeIdentifier = getDegreeIdentifier(degreeItem);
        if (degreeIdentifier == null || degreeCollectionIdentifiers.includes(degreeIdentifier)) {
          return false;
        }
        degreeCollectionIdentifiers.push(degreeIdentifier);
        return true;
      });
      return [...degreesToAdd, ...degreeCollection];
    }
    return degreeCollection;
  }
}
