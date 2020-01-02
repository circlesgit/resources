FROM golang:1.12 as build

WORKDIR /go/src/app
COPY . .
ENV GO111MODULE=on
RUN go mod init
RUN go build -o githubStats

FROM gcr.io/distroless/base
COPY --from=build /go/src/app/githubStats /
CMD ["/githubStats"]
